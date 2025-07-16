package solutions.techsur.common.microservice.config;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.type.TypeBindings;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.type.TypeModifier;
import org.springframework.data.domain.Page;

import java.lang.reflect.Type;

public class PageModule extends SimpleModule {
	@Override
	public void setupModule(SetupContext context) {
		context.addTypeModifier(new PageModifier());
	}

	static class PageModifier extends TypeModifier {
		@Override
		public JavaType modifyType(JavaType type, Type jdkType, TypeBindings bindings, TypeFactory typeFactory) {
			if (Page.class.isAssignableFrom(type.getRawClass())) {
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
