package io.github.fairyspace.aop.framework;

import io.github.fairyspace.aop.AdvisedSupport;
import io.github.fairyspace.beans.utils.ClassUtils;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;


public class Cglib2AopProxy  implements AopProxy{
    private final AdvisedSupport advised;

    public Cglib2AopProxy(AdvisedSupport advised) {
        this.advised = advised;
    }
    @Override
    public Object getProxy() {
        Enhancer enhancer = new Enhancer();
        Class<?> originClass = advised.getTargetSource().getTarget().getClass();
        originClass=ClassUtils.isCglibProxyClass(originClass)?originClass.getSuperclass():originClass;
        enhancer.setSuperclass(originClass);
        enhancer.setInterfaces(advised.getTargetSource().getTargetClass());
        enhancer.setCallback(new DynamicAdvisedInterceptor(advised));
        return enhancer.create();
    }

    private static class DynamicAdvisedInterceptor implements MethodInterceptor{
        private final AdvisedSupport advised;

        public DynamicAdvisedInterceptor(AdvisedSupport advised) {
            this.advised = advised;
        }

        @Override
        public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {

            //aop实现
            CglibMethodInvocation methodInvocation = new CglibMethodInvocation(advised.getTargetSource().getTarget(), method, objects, methodProxy);
            if (advised.getMethodMatcher().matches(method, advised.getTargetSource().getTarget().getClass())) {
                //aliance
                return advised.getMethodInterceptor().invoke(methodInvocation);
            }
            return methodInvocation.proceed();
//cglib 实现
//            Object invoke = methodProxy.invoke(advised.getTargetSource().getTarget(), objects);
//            return invoke;

        }
    }

    private static class CglibMethodInvocation extends  ReflectiveMethodInvocation{
        private final MethodProxy methodProxy;

        public CglibMethodInvocation(Object target, Method method, Object[] arguments, MethodProxy methodProxy) {
            super(target, method, arguments);
            this.methodProxy = methodProxy;
        }

        @Override
        public Object proceed() throws Throwable {
            return this.methodProxy.invoke(this.target, this.arguments);
        }
    }
}
