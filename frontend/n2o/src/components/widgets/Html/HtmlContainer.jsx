import widgetContainer from '../WidgetContainer';
import Html from './Html';
import { HTML } from '../widgetTypes';

export default widgetContainer(
  {
    mapProps: props => {
      return {
        widgetId: props.widgetId,
        html: props.html,
        url: props.url
      };
    }
  },
  HTML
)(Html);
