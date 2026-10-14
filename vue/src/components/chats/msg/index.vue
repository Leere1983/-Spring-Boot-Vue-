<script>
import { h} from "vue";
import txt from './txt.vue';

export default {
  props: {
    msg: Object
  },
  render() {
    const msg = this.msg;
    switch (msg.type) {
      case 'txt':
        return h(txt, {
          msg
        });
    }
    return h('span', {}, ['不支持此消息类型']);
  }
}
</script>