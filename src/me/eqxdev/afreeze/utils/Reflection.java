package me.eqxdev.afreeze.utils;

import org.bukkit.Bukkit;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class Reflection {

    public static final String VERSION =
            Bukkit.getServer().getClass().getPackage().getName()
                    .substring(Bukkit.getServer().getClass().getPackage().getName().lastIndexOf('.') + 1);

    public static SafeClass getSafeClass(Class<?> cls) {
        return new SafeClass(cls);
    }

    public static SafeClass getSafeClass(Object object) {
        return new SafeClass(object.getClass());
    }

    public static SafeClass getSafeClass(String name) {
        try {
            name = name.replace("{v}", VERSION).replace("{nm}", "net.minecraft").replace("{nms}", "net.minecraft.server").replace("{ob}", "org.bukkit").replace("{obc}", "org.bukkit.craftbukkit");
            return getSafeClass(Class.forName(name));
        } catch (ClassNotFoundException ignored) {
        }
        return null;
    }

    public static class SafeClass implements Cloneable {

        private Class<?> cls;
        private Object object;

        private Map<String, SafeField> LOADED_FIELDS = new HashMap<>();
        private Map<String, SafeMethod> LOADED_METHODS = new HashMap<>();

        protected SafeClass(Class<?> cls) {
            this.cls = cls;

            for (Field field : this.cls.getDeclaredFields()) {
                LOADED_FIELDS.put(field.getName(), new SafeField(this, field));
            }
            for (Method method : this.cls.getDeclaredMethods()) {
                LOADED_METHODS.put(method.getName(), new SafeMethod(this, method));
            }
        }

        public Class<?> getRepresentedClass() {
            return this.cls;
        }

        public void setObject(Object object) {
            this.object = object;
        }

        public Object getObject() {
            return this.object;
        }

        public SafeConstructor getConstructor(Object... parameters) {
            Class<?>[] classes = new Class<?>[parameters.length];
            for (int i = 0; i < parameters.length; i++) {
                classes[i] = parameters[i].getClass();
            }
            try {
                return new SafeConstructor(this, cls.getConstructor(classes));
            } catch (ReflectiveOperationException e) {
            }
            return null;
        }

        public SafeConstructor getConstructor(Class<?>... classes) {
            try {
                return new SafeConstructor(this, cls.getConstructor(classes));
            } catch (ReflectiveOperationException ignored) {
            }
            return null;
        }

        public SafeField getField(String name) {
            return LOADED_FIELDS.get(name);
        }

        public SafeMethod getMethod(String name) {
            return LOADED_METHODS.get(name);
        }

        public SafeClass clone() {
            try {
                return (SafeClass) super.clone();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

    }

    public static class SafeField implements Cloneable {

        private SafeClass sc;
        private Field field;
        private Object ret;
        private boolean revertAccessibility = false;

        protected SafeField(SafeClass sc, Field field) {
            try {
                this.sc = sc;
                this.field = field;
                if (!this.field.isAccessible()) {
                    this.revertAccessibility = true;
                }
                this.field.setAccessible(true);
                if (sc.getObject() != null) this.ret = this.field.get(sc.getObject());
                if (revertAccessibility) this.field.setAccessible(false);
            } catch (ReflectiveOperationException ignored) {
            }
        }

        public SafeClass getSafeClass() {
            return this.sc;
        }

        public Field getRepresentedField() {
            return this.field;
        }

        public void set(Object object) {
            this.field.setAccessible(true);
            try {
                this.field.set(sc.getObject(), object);
            } catch (ReflectiveOperationException ignored) {
            }
            if (this.revertAccessibility) this.field.setAccessible(false);
        }

        public Object get() {
            if (this.ret == null && sc.getObject() != null) {
                try {
                    field.setAccessible(true);
                    if (sc.getObject() != null) this.ret = field.get(sc.getObject());
                    if (revertAccessibility) field.setAccessible(false);
                } catch (ReflectiveOperationException ignored) {
                }
            }
            return this.ret;
        }

        public byte getByte() {
            return (byte) this.ret;
        }

        public char getChar() {
            return (char) this.ret;
        }

        public short getShort() {
            return (short) this.ret;
        }

        public int getInt() {
            return (int) this.ret;
        }

        public long getLong() {
            return (long) this.ret;
        }

        public float getFloat() {
            return (float) this.ret;
        }

        public double getDouble() {
            return (double) this.ret;
        }

        public boolean getBoolean() {
            return (boolean) this.ret;
        }

        public String getString() {
            return (String) this.ret;
        }

        public SafeField clone() {
            try {
                return (SafeField) super.clone();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

    }

    public static class SafeMethod implements Cloneable {

        private SafeClass sc;
        private Method method;
        private Object ret;
        private boolean revertAccessibility = false;

        protected SafeMethod(SafeClass sc, Method method) {
            this.sc = sc;
            this.method = method;
            if (!this.method.isAccessible()) this.revertAccessibility = true;
        }

        public Object invoke(Object... parameters) {
            this.method.setAccessible(true);

            try {
                if (sc.getObject() != null) {
                    this.ret = method.invoke(sc.getObject(), parameters);
                } else this.ret = method.invoke(null, parameters);
            } catch (ReflectiveOperationException ignored) {
            }

            if (this.revertAccessibility) this.method.setAccessible(false);
            return this.ret;
        }

        public Object get() {
            return this.ret;
        }

        public Method getRepresentedMethod() {
            return this.method;
        }

        public SafeClass getSafeClass() {
            return this.sc;
        }

        public SafeMethod clone() {
            try {
                return (SafeMethod) super.clone();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

    }

    public static class SafeConstructor {

        private SafeClass sc;
        private Constructor constructor;
        private boolean revertAccessibility = false;

        protected SafeConstructor(SafeClass sc, Constructor constructor) {
            this.sc = sc;
            this.constructor = constructor;
            if (!this.constructor.isAccessible()) this.revertAccessibility = true;
        }

        public Object newInstance(Object... parameters) {
            this.constructor.setAccessible(true);

            Object ret = null;
            try {
                ret = constructor.newInstance(parameters);
            } catch (ReflectiveOperationException ignored) {
            }

            if (revertAccessibility) this.constructor.setAccessible(false);
            return ret;
        }

        public Constructor getRepresentedConstructor() {
            return this.constructor;
        }

        public SafeClass getSafeClass() {
            return this.sc;
        }

        public SafeConstructor clone() {
            try {
                return (SafeConstructor) super.clone();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

    }

}
