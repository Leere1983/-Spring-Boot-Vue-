import EImg from "./e-img.vue";
import EImages from "./e-images.vue";

const install = (app) => {
    app.component("e-img", EImg);
    app.component("e-img-box", EImg);
    app.component("e-images", EImages);
};

export default {
    install,
};
