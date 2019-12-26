import React from 'react';
import PropTypes from 'prop-types';
import { compose, withHandlers, mapProps, branch } from 'recompose';
import get from 'lodash/get';

import SimpleButton from '../../../../buttons/Simple/Simple';
import withActionButton from '../../../../buttons/withActionButton';
import withCell from '../../withCell';
import { withLinkAction } from '../../../../buttons/Link/Link';

function LinkCell({ url, ...props }) {
  return <SimpleButton {...props} href={url} tag="a" />;
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
      src: rest.url ? 'LinkButton' : 'PerformButton',
      label: get(model, fieldKey || id, ''),
      color: 'link',
      model,
    }),
  }),
  mapProps(({ createButton, ...rest }) => ({
    ...createButton(),
    ...rest,
  })),
  branch(
    ({ action }) => action,
    withActionButton({
      onClick: (e, { action, resolveWidget, model, dispatch }) => {
        resolveWidget(model);
        dispatch(action);
      },
    }),
    withLinkAction
  )
);

export { LinkCell };
export default enhance(LinkCell);
