import EContainer from "./container.vue";
import EModuleInfo from "./module-info.vue";
const install = (app) => {
    app.component(EContainer.name, EContainer);
    app.component(EModuleInfo.name, EModuleInfo);
};

export default {
    install,
    EContainer,
};
