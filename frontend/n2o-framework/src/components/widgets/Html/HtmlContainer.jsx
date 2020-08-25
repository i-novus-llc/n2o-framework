import React from 'react';
import widgetContainer from '../WidgetContainer';
import Html from './Html';
import { HTML } from '../widgetTypes';
import 'whatwg-fetch';

function HtmlContainer(props) {
  return <Html {...props} />;
}

export default widgetContainer(
  {
    mapProps: props => ({
      ...props,
      data: props.datasource && props.datasource[0],
      loading: props.isLoading,
    }),
  },
  HTML
)(HtmlContainer);
