package com.twopizzas.di;

import com.twopizzas.util.AssertionConcern;

import java.util.Collection;
import java.util.stream.Collectors;

class ProfileBeanResolver extends AssertionConcern implements BeanResolver {

    private final BeanResolver wrapped;
    private final String profile;

    public ProfileBeanResolver(BeanResolver wrapped, String profile) {
        this.wrapped = wrapped;
        this.profile = profile;
    }

    // for testing only
    public String getProfile() {
        return profile;
    }

    @Override
    public <T> Collection<Bean<T>> resolve(TypedComponentSpecification<T> specification, Collection<Bean<?>> beans) {
        Collection<Bean<T>> resolved = wrapped.resolve(specification, beans);
        Collection<Bean<T>> profileBeans = resolved.stream().filter(b -> b.getProfiles().contains(profile)).collect(Collectors.toSet());
        if (!profileBeans.isEmpty()) {
            return profileBeans;
        }
        return resolved;
    }
}
