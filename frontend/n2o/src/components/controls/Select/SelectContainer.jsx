import React from 'react';
import PropTypes from 'prop-types';
import Select from './Select';
import Option from './Option';
import mapProp from '../../../utils/mapProp';

/**
 * Контейнер для Select(пропсы прокидывает)
 * @reactProps {object} select - пропсы для Select
 * @reactProps {array} options -  пропсы для Option
 */
class SelectContainer extends React.Component {
  /**
   * Базовый рендер
   **/
  render() {
    const { options, select, ...props } = this.props;
    return (
      <Select {...select} {...props}>
        {mapProp(options).map(option => (
          <Option {...option} />
        ))}
      </Select>
    );
  }
}

SelectContainer.propTypes = {
  options: PropTypes.array,
  select: PropTypes.object,
};

SelectContainer.defaultProps = {
  options: [],
};

export default SelectContainer;
