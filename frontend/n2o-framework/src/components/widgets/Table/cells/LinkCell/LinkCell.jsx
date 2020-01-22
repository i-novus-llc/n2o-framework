import React from 'react';
import PropTypes from 'prop-types';
import { compose, withHandlers, mapProps } from 'recompose';
import get from 'lodash/get';

import withCell from '../../withCell';
import StandardButton from '../../../../buttons/StandardButton/StandardButton';

function LinkCell({ url, ...props }) {
  return <StandardButton {...props} url={url} href={url} />;
}

LinkCell.contextTypes = {
  store: PropTypes.object,
};

const enhance = compose(
  withCell,
  withHandlers({
    createButton: ({
      widgetId,
      dispatch,
      columnId,
      model,
      className,
      fieldKey,
      id,
      resolveWidget,
      ...rest
    }) => () => ({
      id,
      className,
      label: get(model, fieldKey || id, ''),
      color: 'link',
      model,
    }),
  }),
  mapProps(({ createButton, ...rest }) => ({
    ...createButton(),
    ...rest,
  }))
);

export { LinkCell };
export default enhance(LinkCell);
