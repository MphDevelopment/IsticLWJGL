package Annotation;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.lang.model.element.Element;
import javax.lang.model.element.NestingKind;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import java.util.Set;

@SupportedAnnotationTypes("Annotation.OnlyGLFWWindowCallable")
public class AnnotationProcessor extends AbstractProcessor
{
    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv)
    {
        for (Element element : roundEnv.getElementsAnnotatedWith(OnlyGLFWWindowCallable.class)) {

                /*if (element.getKind() != ElementKind.CLASS) {
                    processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, "Can be applied to class.");
                    return true;
                }*/

            TypeElement typeElement = (TypeElement) element;

            NestingKind nk = typeElement.getNestingKind();

            if (!nk.name().equals("GLFWWindow")) {
                processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, "'"+nk.name()+"' can't be used outside 'GLFWWindow'");
                return true;
            }
        }

        return true;
    }

    public static void main(String args[]) {

    }
}
