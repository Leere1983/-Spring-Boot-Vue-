<template>
    <div class="footer mt10">
        <div class="copyrightnr">
            地址：xxxxxxxxxxxxx　　电话：0000-00000000　　手机：12312312312
            <br />
            版权所有：{{$order.title}} 　　ICP备********号
        </div>
    </div>
</template>

<style type="text/scss" scoped lang="scss">
    .footer {
        padding: 15px;
        color: #767676;
        text-align: center;
        .link {
            a:after {
                content: " | ";
            }
            a:last-child:after {
                content: "";
            }
        }
    }
</style>
<script>
    import DB from "@/utils/db";
    import config from "@/config";
    export default {
        data() {
            return {
                $order: config,
                likeList: [],
            };
        },
        methods: {},
        created() {},
    };
</script>
