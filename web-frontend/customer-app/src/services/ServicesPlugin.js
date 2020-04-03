export const ServicesPlugin = {
    install(Vue, {services}) {
        Vue.prototype.$services = services;
    }
};

export default ServicesPlugin;
