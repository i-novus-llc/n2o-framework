import React from 'react';
import PropTypes from 'prop-types';
import RadioGroup from './RadioGroup';
import Radio from '../Radio/RadioN2O';
import mapProp from '../../../utils/mapProp';

/**
 * Контейнер для RadioGroup(пропсы прокидывает)
 * @reactProps {object} radioGroup - пропсы для RadioGroup
 * @reactProps {array} radios -  пропсы для Radio
 */
class RadioGroupContainer extends React.Component {
  /**
   * Базовый рендер
   **/
  render() {
    const { radioGroup, radios, ...props } = this.props;
    return (
      <RadioGroup {...radioGroup} {...props}>
        {mapProp(radios).map(radio => (
          <Radio {...radio} />
        ))}
      </RadioGroup>
    );
  }
}

RadioGroupContainer.propTypes = {
  radioGroup: PropTypes.object,
  radios: PropTypes.array,
};

RadioGroupContainer.defaultProps = {
  radios: [],
};

export default RadioGroupContainer;
