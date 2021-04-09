import React, { useMemo } from 'react';
import get from 'lodash/get';
import omit from 'lodash/omit';
import { isEmpty } from "lodash";

import withCell from '../../withCell';
import withTooltip from '../../withTooltip';
import StandardButton from '../../../../buttons/StandardButton/StandardButton';

import { LinkCellType } from './linkCellTypes';
import {compose, withHandlers} from "recompose";

function LinkCell(props) {
  const {
    widgetId,
    dispatch,
    columnId,
    model,
    className,
    fieldKey,
    id,
    resolveWidget,
    icon,
    type,
    url,
    onResolve,
    ...rest
  } = props;

  const submitType = useMemo(() => {
    let content = {
      icon: icon,
      label: get(model, fieldKey || id, ''),
    };

    if (type === LinkCellType.TEXT) {
      content = {
        label: get(model, fieldKey || id, ''),
      };
    } else if (type === LinkCellType.ICON) {
      content = { icon };
    }

    return content;
  }, [type, model, fieldKey, id, icon]);

  return (
    <span
      onClick={onResolve}
    >
      <StandardButton
        id={id}
        className={className}
        color={'link'}
        model={model}
        entityKey={widgetId}
        {...submitType}
        {...omit(rest, ['icon', 'label'])}
        url={url}
        href={url}
      />
    </span>
  );
}

export { LinkCell };
export default compose(
  withCell,
  withTooltip,
  withHandlers({
    onResolve: ({ callAction, model, action }) => () => {
      if (callAction && model && isEmpty(action)) {
        callAction(model);
      }
    },
  })
)(LinkCell);
