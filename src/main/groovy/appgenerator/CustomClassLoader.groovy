package appgenerator

import groovy.transform.InheritConstructors
import groovy.transform.CompileStatic
import org.codehaus.groovy.control.CompilationFailedException

/**
 * Created by jameskleeh on 5/11/17.
 */
@InheritConstructors
@CompileStatic
class CustomClassLoader extends GroovyClassLoader {

    public Class loadClass(final String name, boolean lookupScriptFiles, boolean preferClassOverScript, boolean resolve)
            throws ClassNotFoundException, CompilationFailedException {
        // look into cache
        Class cls = getClassCacheEntry(name);

        // enable recompilation?
        boolean recompile = isRecompilable(cls);
        if (!recompile) return cls;

        ClassNotFoundException last = null;

        // check security manager
        SecurityManager sm = System.getSecurityManager();
        if (sm != null) {
            String className = name.replace('/', '.');
            int i = className.lastIndexOf('.');
            // no checks on the sun.reflect classes for reflection speed-up
            // in particular ConstructorAccessorImpl, MethodAccessorImpl, FieldAccessorImpl and SerializationConstructorAccessorImpl
            // which are generated at runtime by the JDK
            if (i != -1 && !className.startsWith("sun.reflect.")) {
                sm.checkPackageAccess(className.substring(0, i));
            }
        }

        // prefer class if no recompilation
        if (cls != null && preferClassOverScript) return cls;

        // at this point the loading from a parent loader failed
        // and we want to recompile if needed.
        if (lookupScriptFiles) {
            // try groovy file
            try {
                // check if recompilation already happened.
                final Class classCacheEntry = getClassCacheEntry(name);
                if (classCacheEntry != cls) return classCacheEntry;
                URL source = resourceLoader.loadGroovySource(name);
                // if recompilation fails, we want cls==null
                Class oldClass = cls;
                cls = null;
                cls = super.recompile(source, name, oldClass);
            } catch (IOException ioe) {
                last = new ClassNotFoundException("IOException while opening groovy source: " + name, ioe);
            } finally {
                if (cls == null) {
                    removeClassCacheEntry(name);
                } else {
                    setClassCacheEntry(cls);
                }
            }
        }

        if (cls == null) {
            throw new ClassNotFoundException(name)
        }
        return cls;
    }
}
