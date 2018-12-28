import React from 'react';
import PropTypes from 'prop-types';
import { omit } from 'lodash';
import pure from 'recompose/pure';
import mapProps from 'recompose/mapProps';
import propsResolver from '../../../utils/propsResolver';
import getElementType from '../../../tools/getElementType';

/**
 * Ячейка таблицы
 * @reactProps {string} className - имя css класса
 * @reactProps {object} style - css стиль
 * @reactProps {element} component - React класс компонента cell
 * @reactProps {string} as - Тип элемента для рендеринга
 * @reactProps {object} model - Модель строки
 * @reactProps {number} colSpan - colSpan таблицы
 * @reactprops {node} children - элемент потомок компонента TableCell
 */
class TableCell extends React.Component {
  getPassProps() {
    return omit(this.props, ['component', 'className', 'style', 'colSpan', 'as']);
  }

  render() {
    const { className, style, component, colSpan, children, model } = this.props;
    const ElementType = getElementType(TableCell, this.props);
    if (React.Children.count(children)) {
      return (
        <ElementType className={className} colSpan={colSpan} model={model} style={style}>
          {children}
        </ElementType>
      );
    }
    const tableCellBody = React.createElement(component, {
      ...propsResolver(this.getPassProps(), model),
      model
    });
    return (
      <ElementType className={className} model={model} colSpan={colSpan} style={style}>
        {tableCellBody}
      </ElementType>
    );
  }
}

TableCell.propTypes = {
  /* Default props */
  className: PropTypes.string,
  style: PropTypes.string,
  children: PropTypes.node,
  /* Specific props */
  component: PropTypes.element,
  as: PropTypes.string,
  model: PropTypes.object,
  colSpan: PropTypes.number
};

TableCell.defaultProps = {
  as: 'td'
};

export default TableCell;
