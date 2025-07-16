package solutions.techsur.common.microservice.config;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.type.TypeBindings;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.type.TypeModifier;
import org.springframework.data.domain.Page;

import java.lang.reflect.Type;

public class PageModule extends SimpleModule {

	private static final long serialVersionUID = 1L;

	@Override
	public void setupModule(SetupContext context) {
		if (context != null) {
			context.addTypeModifier(new PageModifier());
		}
	}

	private static final class PageModifier extends TypeModifier {

		@Override
		public JavaType modifyType(final JavaType type, final Type jdkType, final TypeBindings bindings, final TypeFactory typeFactory) {
			// Check if the type is assignable to Page
			if (Page.class.isAssignableFrom(type.getRawClass())) {
				final JavaType[] params = typeFactory.findTypeParameters(type, Page.class);
				// If no generic parameter was found, fallback to unknown reference type
				if (params == null || params.length == 0) {
					return typeFactory.constructReferenceType(type.getRawClass(), TypeFactory.unknownType());
				}
				// Construct a collection-like type with the found generic parameter
				return typeFactory.constructCollectionLikeType(type.getRawClass(), params[0]);
			}
			return type;
		}
	}
}