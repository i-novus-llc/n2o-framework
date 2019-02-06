import React from 'react';
import PropTypes from 'prop-types';

import CheckboxGroup from './CheckboxGroup';
import Checkbox from '../Checkbox/Checkbox';
import withFetchData from '../withFetchData.js';
import CheckboxN2O from '../Checkbox/CheckboxN2O';
import CheckboxButton from '../Checkbox/CheckboxButton';
import Spinner from '../../snippets/Spinner/InlineSpinner';

/**
 * Wrapper для группы чекбоксов
 * @reactProps {array} data - данные для чекбоксов
 * @reactProps {string} valueFieldId - ключ для value в data
 * @reactProps {string} labelFieldId - ключ для label в дата
 * @reactProps {array} value - выбранное значение
 * @reactProps {function} onChange - вызывается при изменении значения
 * @reactProps {boolean} disabled - только для чтения
 * @reactProps {boolean} visible - флаг видимости
 * @reactProps {boolean} inline - флаг вывода в ряд
 * @reactProps {object} style - стили группы
 * @reactProps {string} className - класс группы
 * @reactProps {function} fetchData - функция для получения данных
 * @reactProps {string} queryId - queryId
 * @reactProps {number} size - размер
 * @reactProps {string} type - тип чекбокса
 * @reactProps {boolean} isLoading - флаг готовности
 */

class CheckboxGroupControl extends React.Component {
  componentDidMount() {
    this.props._fetchData({
      size: this.props.size,
      [`sorting.${this.props.labelFieldId}`]: 'ASC'
    });
  }

  /**
   * Рендер
   */

  render() {
    const { data, labelFieldId, type, isLoading } = this.props;

    const checkboxTypes = {
      default: Checkbox,
      n2o: CheckboxN2O,
      btn: CheckboxButton
    };

    const CheckboxElement = checkboxTypes[type];

    return (
      <React.Fragment>
        {!isLoading && (
          <CheckboxGroup {...this.props}>
            {data &&
              data.map(checkbox => (
                <CheckboxElement
                  key={checkbox.id}
                  value={checkbox}
                  label={checkbox[labelFieldId]}
                  disabled={checkbox.disabled}
                  checked={checkbox.checked}
                />
              ))}
          </CheckboxGroup>
        )}
        {isLoading && <Spinner />}
      </React.Fragment>
    );
  }
}

CheckboxGroupControl.propTypes = {
  data: PropTypes.array,
  valueFieldId: PropTypes.string,
  labelFieldId: PropTypes.string,
  value: PropTypes.array,
  onChange: PropTypes.func,
  disabled: PropTypes.bool,
  visible: PropTypes.bool,
  inline: PropTypes.bool,
  style: PropTypes.object,
  className: PropTypes.string,
  fetchData: PropTypes.func,
  queryId: PropTypes.string.isRequired,
  size: PropTypes.number.isRequired,
  type: PropTypes.oneOf(['default', 'n2o', 'btn']),
  isLoading: PropTypes.bool
};

CheckboxGroupControl.defaultProps = {
  data: [],
  value: [],
  visible: true,
  type: 'default',
  onChange: () => {},
  isLoading: false
};

export default withFetchData(CheckboxGroupControl);
