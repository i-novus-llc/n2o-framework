import React from 'react';
import { compose, mapProps } from 'recompose';

import SimpleButton from '../Simple/Simple';
import mappingProps from '../Simple/mappingProps';
import withActionButton from '../withActionButton';

export default compose(
  withActionButton({
    onClick: (e, props) => {
      props.dispatch(props.action);
    }
  }),
  mapProps(props => mappingProps(props))
)(SimpleButton);
