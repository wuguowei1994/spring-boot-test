//package com.example.demo.converter;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import lombok.RequiredArgsConstructor;
//import org.springframework.core.CollectionFactory;
//import org.springframework.core.convert.ConversionService;
//import org.springframework.core.convert.support.DefaultConversionService;
//import org.springframework.core.convert.support.GenericConversionService;
//import org.springframework.data.convert.CustomConversions;
//import org.springframework.data.convert.EntityConverter;
//import org.springframework.data.elasticsearch.core.convert.ElasticsearchCustomConversions;
//import org.springframework.data.elasticsearch.core.convert.ElasticsearchTypeMapper;
//import org.springframework.data.elasticsearch.core.mapping.ElasticsearchPersistentEntity;
//import org.springframework.data.elasticsearch.core.mapping.ElasticsearchPersistentProperty;
//import org.springframework.data.mapping.PersistentPropertyAccessor;
//import org.springframework.data.mapping.context.MappingContext;
//import org.springframework.data.mapping.model.*;
//import org.springframework.data.util.ClassTypeInformation;
//import org.springframework.data.util.TypeInformation;
//import org.springframework.lang.Nullable;
//import org.springframework.util.ClassUtils;
//
//import java.util.*;
//
//public class ElasticsearchEntityMapper implements
//        EntityConverter<ElasticsearchPersistentEntity<?>, ElasticsearchPersistentProperty, Object, Map<String, Object>> {
//
//    private final MappingContext<? extends ElasticsearchPersistentEntity<?>, ElasticsearchPersistentProperty> mappingContext;
//    private final GenericConversionService conversionService;
//    private ElasticsearchTypeMapper typeMapper;
//    private CustomConversions conversions = new ElasticsearchCustomConversions(Collections.emptyList());
//    private EntityInstantiators instantiators = new EntityInstantiators();
//
//    public ElasticsearchEntityMapper(
//            MappingContext<? extends ElasticsearchPersistentEntity<?>, ElasticsearchPersistentProperty> mappingContext,
//            @Nullable GenericConversionService conversionService) {
//        this.mappingContext = mappingContext;
//        this.typeMapper = ElasticsearchTypeMapper.create(mappingContext);
//        this.conversionService = conversionService;
//    }
//
//    public void setTypeMapper(ElasticsearchTypeMapper typeMapper) {
//        this.typeMapper = typeMapper;
//    }
//
//    @Override
//    public MappingContext<? extends ElasticsearchPersistentEntity<?>, ElasticsearchPersistentProperty> getMappingContext() {
//        return null;
//    }
//
//    @Override
//    public ConversionService getConversionService() {
//        return conversionService;
//    }
//
//    public void setConversions(CustomConversions conversions) {
//        this.conversions = conversions;
//    }
//
//    @Override
//    public <R> R read(Class<R> type, Map<String, Object> source) {
//        return doRead(source, ClassTypeInformation.from((Class<R>) ClassUtils.getUserClass(type)));
//    }
//
//    @SuppressWarnings("unchecked")
//    @Nullable
//    protected <R> R doRead(Map<String, Object> source, TypeInformation<R> typeHint) {
//
//        if (source == null) {
//            return null;
//        }
//
//        typeHint = (TypeInformation<R>) typeMapper.readType(source, typeHint);
//
//        if (conversions.hasCustomReadTarget(Map.class, typeHint.getType())) {
//            return conversionService.convert(source, typeHint.getType());
//        }
//
//        if (typeHint.isMap() || ClassTypeInformation.OBJECT.equals(typeHint)) {
//            return (R) source;
//        }
//
//        ElasticsearchPersistentEntity<?> entity = mappingContext.getRequiredPersistentEntity(typeHint);
//        return readEntity(entity, source);
//    }
//
//    @SuppressWarnings("unchecked")
//    protected <R> R readEntity(ElasticsearchPersistentEntity<?> entity, Map<String, Object> source) {
//
//        ElasticsearchPersistentEntity<?> targetEntity = computeClosestEntity(entity, source);
//
//        ElasticsearchPropertyValueProvider propertyValueProvider = new ElasticsearchPropertyValueProvider(
//                new MapValueAccessor(source));
//
//        EntityInstantiator instantiator = instantiators.getInstantiatorFor(targetEntity);
//
//        R instance = (R) instantiator.createInstance(targetEntity,
//                new PersistentEntityParameterValueProvider<>(targetEntity, propertyValueProvider, null));
//
//        return targetEntity.requiresPropertyPopulation() ? readProperties(targetEntity, instance, propertyValueProvider)
//                : instance;
//    }
//
//    protected <R> R readProperties(ElasticsearchPersistentEntity<?> entity, R instance,
//                                   ElasticsearchPropertyValueProvider valueProvider) {
//
//        PersistentPropertyAccessor<R> accessor = new ConvertingPropertyAccessor<>(entity.getPropertyAccessor(instance),
//                conversionService);
//
//        for (ElasticsearchPersistentProperty prop : entity) {
//
//            if (entity.isConstructorArgument(prop) || prop.isScoreProperty()) {
//                continue;
//            }
//
//            Object value = valueProvider.getPropertyValue(prop);
//            if (value != null) {
//                accessor.setProperty(prop, valueProvider.getPropertyValue(prop));
//            }
//        }
//
//        return accessor.getBean();
//    }
//
//    private ElasticsearchPersistentEntity<?> computeClosestEntity(ElasticsearchPersistentEntity<?> entity,
//                                                                  Map<String, Object> source) {
//
//        TypeInformation<?> typeToUse = typeMapper.readType(source);
//
//        if (typeToUse == null) {
//            return entity;
//        }
//
//        if (!entity.getTypeInformation().getType().isInterface() && !entity.getTypeInformation().isCollectionLike()
//                && !entity.getTypeInformation().isMap()
//                && !ClassUtils.isAssignableValue(entity.getType(), typeToUse.getType())) {
//            return entity;
//        }
//
//        return mappingContext.getRequiredPersistentEntity(typeToUse);
//    }
//
//    @RequiredArgsConstructor
//    class ElasticsearchPropertyValueProvider implements PropertyValueProvider<ElasticsearchPersistentProperty> {
//
//        final MapValueAccessor mapValueAccessor;
//
//        @SuppressWarnings("unchecked")
//        @Override
//        public <T> T getPropertyValue(ElasticsearchPersistentProperty property) {
//            return (T) readValue(mapValueAccessor.get(property), property, property.getTypeInformation());
//        }
//
//    }
//
//    @SuppressWarnings("unchecked")
//    protected <R> R readValue(@Nullable Object source, ElasticsearchPersistentProperty property,
//                              TypeInformation<R> targetType) {
//
//        if (source == null) {
//            return null;
//        }
//
//        Class<R> rawType = targetType.getType();
//        if (conversions.hasCustomReadTarget(source.getClass(), rawType)) {
//            return rawType.cast(conversionService.convert(source, rawType));
//        } else if (source instanceof List) {
//            return readCollectionValue((List) source, property, targetType);
//        } else if (source instanceof Map) {
//            return readMapValue((Map<String, Object>) source, property, targetType);
//        }
//
//        return (R) readSimpleValue(source, targetType);
//    }
//
//    private Collection<Object> createCollectionForValue(TypeInformation<?> collectionTypeInformation, int size) {
//
//        Class<?> collectionType = collectionTypeInformation.isSubTypeOf(Collection.class) //
//                ? collectionTypeInformation.getType() //
//                : List.class;
//
//        TypeInformation<?> componentType = collectionTypeInformation.getComponentType() != null //
//                ? collectionTypeInformation.getComponentType() //
//                : ClassTypeInformation.OBJECT;
//
//        return collectionTypeInformation.getType().isArray() //
//                ? new ArrayList<>(size) //
//                : CollectionFactory.createCollection(collectionType, componentType.getType(), size);
//    }
//
//    private boolean isSimpleType(Object value) {
//        return isSimpleType(value.getClass());
//    }
//
//    private boolean isSimpleType(Class<?> type) {
//        return conversions.isSimpleType(type);
//    }
//
//    @SuppressWarnings("unchecked")
//    private Object readSimpleValue(@Nullable Object value, TypeInformation<?> targetType) {
//
//        Class<?> target = targetType.getType();
//
//        if (value == null || target == null || ClassUtils.isAssignableValue(target, value)) {
//            return value;
//        }
//
//        if (conversions.hasCustomReadTarget(value.getClass(), target)) {
//            return conversionService.convert(value, target);
//        }
//
//        if (Enum.class.isAssignableFrom(target)) {
//            return Enum.valueOf((Class<Enum>) target, value.toString());
//        }
//
//        return conversionService.convert(value, target);
//    }
//
//    @SuppressWarnings("unchecked")
//    private <R> R readCollectionValue(@Nullable List<?> source, ElasticsearchPersistentProperty property,
//                                      TypeInformation<R> targetType) {
//
//        if (source == null) {
//            return null;
//        }
//
//        Collection<Object> target = createCollectionForValue(targetType, source.size());
//
//        for (Object value : source) {
//
//            if (value == null) {
//                target.add(null);
//            } else if (isSimpleType(value)) {
//                target.add(
//                        readSimpleValue(value, targetType.getComponentType() != null ? targetType.getComponentType() : targetType));
//            } else {
//
//                if (value instanceof List) {
//                    target.add(readValue(value, property, property.getTypeInformation().getActualType()));
//                } else {
//                    ElasticsearchPersistentEntity<?> targetEntity = computeGenericValueTypeForRead(property, value);
//                    if (targetEntity.getTypeInformation().isMap()) {
//                        target.add(readMapValue((Map) value, property, targetEntity.getTypeInformation()));
//                    } else {
//                        target.add(readEntity(targetEntity, (Map) value));
//                    }
//                }
//            }
//        }
//
//        return (R) target;
//    }
//
//    @SuppressWarnings("unchecked")
//    private <R> R readMapValue(Map<String, Object> source, ElasticsearchPersistentProperty property,
//                               TypeInformation<R> targetType) {
//
//        TypeInformation information = typeMapper.readType(source);
//        if (property.isEntity() && !property.isMap() || information != null) {
//
//            ElasticsearchPersistentEntity<?> targetEntity = information != null
//                    ? mappingContext.getRequiredPersistentEntity(information)
//                    : mappingContext.getRequiredPersistentEntity(property);
//            return readEntity(targetEntity, source);
//        }
//
//        Map<String, Object> target = new LinkedHashMap<>();
//        for (Map.Entry<String, Object> entry : source.entrySet()) {
//
//            String entryKey = entry.getKey();
//            Object entryValue = entry.getValue();
//
//            if (entryValue == null) {
//                target.put(entryKey, null);
//            } else if (isSimpleType(entryValue)) {
//                target.put(entryKey,
//                        readSimpleValue(entryValue, targetType.isMap() ? targetType.getMapValueType() : targetType));
//            } else {
//
//                ElasticsearchPersistentEntity<?> targetEntity = computeGenericValueTypeForRead(property, entryValue);
//
//                if (targetEntity.getTypeInformation().isMap()) {
//
//                    Map<String, Object> valueMap = (Map) entryValue;
//                    if (typeMapper.containsTypeInformation(valueMap)) {
//                        target.put(entryKey, readEntity(targetEntity, (Map) entryValue));
//                    } else {
//                        target.put(entryKey, readValue(valueMap, property, targetEntity.getTypeInformation()));
//                    }
//
//                } else if (targetEntity.getTypeInformation().isCollectionLike()) {
//                    target.put(entryKey, readValue(entryValue, property, targetEntity.getTypeInformation().getActualType()));
//                } else {
//                    target.put(entryKey, readEntity(targetEntity, (Map) entryValue));
//                }
//            }
//        }
//
//        return (R) target;
//    }
//
//    private ElasticsearchPersistentEntity<?> computeGenericValueTypeForRead(ElasticsearchPersistentProperty property,
//                                                                            Object value) {
//
//        return ClassTypeInformation.OBJECT.equals(property.getTypeInformation().getActualType())
//                ? mappingContext.getRequiredPersistentEntity(value.getClass())
//                : mappingContext.getRequiredPersistentEntity(property.getTypeInformation().getActualType());
//    }
//
//    static class MapValueAccessor {
//
//        final Map<String, Object> target;
//
//        MapValueAccessor(Map<String, Object> target) {
//            this.target = target;
//        }
//
//        public Object get(ElasticsearchPersistentProperty property) {
//
//            String fieldName = property.getFieldName();
//
//            if (!fieldName.contains(".")) {
//                return target.get(fieldName);
//            }
//
//            Iterator<String> parts = Arrays.asList(fieldName.split("\\.")).iterator();
//            Map<String, Object> source = target;
//            Object result = null;
//
//            while (source != null && parts.hasNext()) {
//
//                result = source.get(parts.next());
//
//                if (parts.hasNext()) {
//                    source = getAsMap(result);
//                }
//            }
//
//            return result;
//        }
//
//        @SuppressWarnings("unchecked")
//        private Map<String, Object> getAsMap(Object result) {
//
//            if (result instanceof Map) {
//                return (Map) result;
//            }
//
//            throw new IllegalArgumentException(String.format("%s is not a Map.", result));
//        }
//    }
//
//    @Override
//    public void write(Object source, Map<String, Object> sink) {
//    }
//}
