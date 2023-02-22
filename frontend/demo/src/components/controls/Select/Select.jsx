import React from 'react';
import PropTypes from 'prop-types';
import Select from '@atlaskit/select';

class SelectExample extends React.Component {
  constructor(props) {
    super(props);
    this.state={
      value: this.props.options.find((option) => option.value === this.props.value)
    };

    this.onChange = this.onChange.bind(this);
  }

  onChange(target) {
    this.setState({
      value: target
    });

    if (this.props.onChange) {
      this.onChange(target)
    }
  }

  render() {
    const {
      autoFocus,
      disabled,
      options
    } = this.props;

    return (
      <Select
        isDisabled={disabled}
        value={this.state.value}
        autoFocus={autoFocus}
        options={options}
        onChange={this.onChange}
      />
    );
  }
}

SelectExample.propTypes = {
  onChange: PropTypes.func,
  required: PropTypes.bool,
  autoFocus: PropTypes.bool,
  disabled: PropTypes.bool,
  visible: PropTypes.bool,
  heightSize:PropTypes.oneOf(['input-sm', 'input-lg', '']),
  value: PropTypes.oneOfType([
    PropTypes.string,
    PropTypes.number
  ]),
  options: PropTypes.array,
};

SelectExample.defaultProps = {
  autoFocus: false,
  disabled: false,
  required: false,
  visible: true,
};

export default SelectExample;
