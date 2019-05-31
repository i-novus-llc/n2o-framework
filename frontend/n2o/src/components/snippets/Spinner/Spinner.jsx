import React, { Component, Fragment } from 'react';
import { Spinner } from 'reactstrap';
import cx from 'classnames';
import { eq, values } from 'lodash';
import PropTypes from 'prop-types';

const MODE = {
  DARK: 'dark',
  LIGHT: 'light',
  TRANSPARENT: 'transparent',
};

const TYPE = {
  INLINE: 'inline',
  COVER: 'cover',
};

let Comp = Spinner;

const timer = ms => new Promise(res => setTimeout(res, ms));

class BaseSpinner extends Component {
  constructor(props) {
    super(props);

    this.state = {
      endTimeout: false,
    };

    this.delayTimer = this.delayTimer.bind(this);
  }

  static setSpinner(component) {
    Comp = component;
  }

  async componentDidUpdate(prevProps) {
    if (!prevProps.loading && this.props.loading) {
      await this.delayTimer();
    }
  }

  async componentDidMount() {
    if (this.props.loading) {
      await this.delayTimer();
    }
  }

  async delayTimer() {
    const { endTimeout } = this.state;
    const { delay } = this.props;

    if (delay) {
      this.setState({ endTimeout: false });
      await timer(delay);
      this.setState({ endTimeout: true });
    }
  }

  render() {
    const {
      children,
      type,
      className,
      text,
      mode,
      loading,
      ...rest
    } = this.props;
    const { endTimeout } = this.state;

    const background = eq(TYPE.COVER, type) && {
      'spinner-container--dark': eq(mode, MODE.DARK) && endTimeout && loading,
      'spinner-container--transparent':
        eq(mode, MODE.TRANSPARENT) && endTimeout,
    };

    return (
      <div
        className={cx(`n2o-spinner-wrapper ${type}`, {
          [className]: className,
          ...background,
        })}
        {...rest}
      >
        {endTimeout && loading && (
          <div className="n2o-spinner-container">
            <Comp color="primary" />
            {eq(TYPE.COVER, type) && <div className="loading_text">{text}</div>}
          </div>
        )}
        {children}
      </div>
    );
  }
}

BaseSpinner.propTypes = {
  loading: PropTypes.bool,
  type: PropTypes.oneOf(values(TYPE)),
  delay: PropTypes.number,
  text: PropTypes.string,
  mode: PropTypes.oneOf(values(MODE)),
};

BaseSpinner.defaultProps = {
  loading: true,
  type: 'inline',
  delay: 400,
  text: '',
  mode: 'light',
};

export default BaseSpinner;
