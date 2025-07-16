package solutions.techsur.common.microservice.config;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.type.TypeBindings;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.type.TypeModifier;
import org.springframework.data.domain.Page;

import java.lang.reflect.Type;

/**
 * Custom Jackson module to handle serialization/deserialization of Spring Data Page types.
 */
public class PageModule extends SimpleModule {

    @Override
    public void setupModule(SetupContext context) {
        context.addTypeModifier(new PageModifier());
    }

    /**
     * Modifier that customizes the JavaType of Page<T> to correctly handle generic type information.
     */
    static class PageModifier extends TypeModifier {

        /**
         * Modifies the JavaType for Page types to properly handle generic type parameters.
         *
         * @param type the original JavaType
         * @param jdkType the original JDK Type
         * @param bindings the TypeBindings
         * @param typeFactory the TypeFactory that can construct new types
         * @return the modified JavaType for Page or original type if not applicable
         */
        @Override
        public JavaType modifyType(JavaType type, Type jdkType, TypeBindings bindings, TypeFactory typeFactory) {
            if (type != null && Page.class.isAssignableFrom(type.getRawClass())) {
                JavaType[] params = typeFactory.findTypeParameters(type, Page.class);
                if (params == null || params.length == 0) {
                    return typeFactory.constructReferenceType(type.getRawClass(), TypeFactory.unknownType());
                }
                return typeFactory.constructCollectionLikeType(type.getRawClass(), params[0]);
            }
            return type;
        }
    }
}