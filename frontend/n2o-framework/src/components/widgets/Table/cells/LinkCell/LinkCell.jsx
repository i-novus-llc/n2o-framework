import React from 'react';
import PropTypes from 'prop-types';
import { compose, withHandlers, mapProps } from 'recompose';
import cn from 'classnames';
import omit from 'lodash/omit';
import get from 'lodash/get';

import Toolbar from '../../../../buttons/Toolbar';
import withCell from '../../withCell';

function LinkCell({ widgetId, className, toolbar }) {
  return (
    <Toolbar
      className={cn('n2o-link-cell', className)}
      entityKey={widgetId}
      toolbar={toolbar}
    />
  );
}

LinkCell.contextTypes = {
  store: PropTypes.object,
};

const enhance = compose(
  withCell,
  withHandlers({
    createToolbar: ({
      widgetId,
      dispatch,
      columnId,
      model,
      fieldKey,
      id,
      ...rest
    }) => () => [
      {
        buttons: [
          {
            id,
            src: rest.url ? 'LinkButton' : 'PerformButton',
            label: get(model, fieldKey || id, ''),
            color: 'link',
            ...omit(rest, ['dispatch', 'updateFieldInModel', 'resolveWidget']),
          },
        ],
      },
    ],
  }),
  mapProps(({ createToolbar, ...rest }) => ({
    toolbar: createToolbar(),
    ...rest,
  }))
);

export { LinkCell };
export default enhance(LinkCell);
