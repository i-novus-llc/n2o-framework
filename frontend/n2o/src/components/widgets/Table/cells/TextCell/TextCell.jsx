/**
 * Created by emamoshin on 27.09.2017.
 */
import React from 'react';
import PropTypes from 'prop-types';
import { get } from 'lodash';
import Text from '../../../../snippets/Typography/Text/Text';

/**
 * Текстовая ячейка таблицы
 * @reactProps {object} model - модель данных
 * @reactProps {string} fieldKey - ключ модели для этой ячейки
 */
class TextCell extends React.Component {
  render() {
    const { model, fieldKey, id, visible, preLine, ...rest } = this.props;
    return (
      visible && (
        <Text
          text={model && get(model, fieldKey || id)}
          preLine={preLine}
          {...rest}
        />
      )
    );
  }
}

TextCell.propTypes = {
  model: PropTypes.object,
  fieldKey: PropTypes.string,
  className: PropTypes.string,
  format: PropTypes.string,
  visible: PropTypes.bool,
};

TextCell.defaultProps = {
  visible: true,
};

export default TextCell;
