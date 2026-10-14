<script>
    import { RouterLink, useLink, useRouter } from "vue-router";
    import { computed, h, reactive } from "vue";
    import { isFullPathActive } from "@/router/router-utils";

    const getLinkClass = (propClass, globalClass, defaultClass) => (propClass != null ? propClass : globalClass != null ? globalClass : defaultClass);

    export default {
        props: {
            to: {
                type: [String, Object],
                required: true,
            },
            replace: Boolean,
            activeClass: String,
            // inactiveClass: String,
            exactActiveClass: String,
            custom: Boolean,
            ariaCurrentValue: {
                type: String,
                default: "page",
            },
            tag: {
                type: String,
                default: "a",
            },
        },
        compatConfig: { MODE: 3 },
        setup(props, { slots }) {
            const { options } = useRouter();
            const link = reactive(useLink(props));
            link.isExactActive = link.isExactActive ? isFullPathActive(props.to) : false;

            const elClass = computed(() => ({
                [getLinkClass(props.activeClass, options.linkActiveClass, "router-link-active")]: link.isActive,
                [getLinkClass(props.exactActiveClass, options.linkExactActiveClass, "router-link-exact-active")]: link.isExactActive,
            }));

            return () => {
                const children = slots.default && slots.default(link);
                return props.custom
                    ? children
                    : h(
                          "a",
                          {
                              "aria-current": link.isExactActive ? props.ariaCurrentValue : null,
                              href: link.href,
                              // this would override user added attrs but Vue will still add
                              // the listener, so we end up triggering both
                              onClick: link.navigate,
                              class: elClass.value,
                          },
                          children
                      );
            };
        },
    };
</script>
