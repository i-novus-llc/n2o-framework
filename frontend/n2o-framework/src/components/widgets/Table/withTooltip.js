import React from 'react';
import get from 'lodash/get';
import Tooltip from '../../snippets/Tooltip/Tooltip';

/**
 * HOC, оборачивает Cell добавляя Tooltip
 * @param WrappedComponent оборачиваемый компонент
 * @param model модель данных
 * @param hint подсказка - тело тултипа
 * @param tooltipFieldId ключ по которому резолвится Tooltip и берется hint
 */
export default function withTooltip(WrappedComponent) {
  class TooltipHOC extends React.Component {
    render() {
      const hint = get(this.props.model, 'tooltipFieldId');
      console.warn(this.props);
      return (
        <Tooltip
          label={<WrappedComponent {...this.props} />}
          hint={hint}
          placement={this.props.placement || 'bottom'}
        />
      );
    }
  }
  return TooltipHOC;
}
