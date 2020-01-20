/**
 * Created by emamoshin on 27.09.2017.
 */
import React from 'react';
import PropTypes from 'prop-types';
import get from 'lodash/get';
import Text from '../../../../snippets/Typography/Text/Text';

/** Описание */
function TextCell({
  model,
  fieldKey,
  id,
  visible,
  preLine,
  subTextFieldKey,
  ...rest
}) {
  return (
    visible && (
      <Text
        text={model && get(model, fieldKey || id)}
        subText={model && subTextFieldKey && get(model, subTextFieldKey)}
        preLine={preLine}
        {...rest}
      />
    )
  );
}

TextCell.propTypes = {
  /**
   * Модель данных
   */
  model: PropTypes.object,
  /**
   * Ключ значения из модели
   */
  fieldKey: PropTypes.string,
  /**
   * Класс
   */
  className: PropTypes.string,
  /**
   * Формат
   */
  format: PropTypes.string,
  /**
   * Ключ значения сабтекста из модели
   */
  subTextFieldKey: PropTypes.string,
  /**
   * Формат сабтекста
   */
  subTextFormat: PropTypes.string,
  /**
   * Флаг видимости
   */
  visible: PropTypes.bool,
};

TextCell.defaultProps = {
  visible: true,
};

export default TextCell;
