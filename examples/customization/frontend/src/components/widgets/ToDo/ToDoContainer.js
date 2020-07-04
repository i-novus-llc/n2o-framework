import React from "react";
import PropTypes from "prop-types";
import widgetContainer from 'n2o-framework/lib/components/widgets/WidgetContainer';

import ToDo from './ToDo';

export default widgetContainer(
  {
    mapProps: props => {
      return {
        data: props.datasource,
      };
    }
  },
  'ToDo'
)(ToDo);
