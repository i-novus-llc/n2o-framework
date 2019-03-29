import React from 'react';
import PropTypes from 'prop-types';

import Text from '../../../../snippets/Text/Text';
import Icon from '../../../../snippets/Icon/Icon';
import { iconCellTypes, textPlaceTypes } from './cellTypes';

/**
 * Ячейка таблицы с иконкой
 * @reactProps {string} id - id ячейки
 * @reactProps {object} model - модель строки
 * @reactProps {string} icon - класс иконки
 * @reactProps {string} type - тип ячейки
 * @reactProps {string} textPlace - расположение текста
 */

class IconCell extends React.Component {
  /**
   * Рендер
   */
  render() {
    const text = this.props.model[this.props.id];
    const { visible } = this.props;
    return (
      visible && (
        <div title={text}>
          {this.props.icon && <Icon name={this.props.icon} />}
          {this.props.type === iconCellTypes.ICONANDTEXT && (
            <div
              className="n2o-cell-text"
              style={{
                float: this.props.textPlace === textPlaceTypes.LEFT ? 'left' : null,
                display: 'inline-block'
              }}
            >
              <Text text={text} />
            </div>
          )}
        </div>
      )
    );
  }
}

IconCell.propTypes = {
  id: PropTypes.string.isRequired,
  model: PropTypes.object.isRequired,
  icon: PropTypes.string.isRequired,
  type: PropTypes.oneOf(Object.values(iconCellTypes)),
  textPlace: PropTypes.oneOf(Object.values(textPlaceTypes)),
  visible: PropTypes.bool
};

IconCell.defaultProps = {
  type: iconCellTypes.ICONANDTEXT,
  textPlace: textPlaceTypes.RIGHT,
  visible: true
};

export default IconCell;
