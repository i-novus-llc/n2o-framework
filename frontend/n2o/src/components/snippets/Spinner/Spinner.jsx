import React, { Component, Fragment } from 'react';
import { Spinner } from 'reactstrap';
import cx from 'classnames';
import { eq, values } from 'lodash';
import PropTypes from 'prop-types';

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
    this.renderCoverSpiner = this.renderCoverSpiner.bind(this);
    this.renderLineSpinner = this.renderLineSpinner.bind(this);
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

  renderCoverSpiner() {
    const {
      children,
      className,
      text,
      loading,
      transparent,
      color,
      ...rest
    } = this.props;
    const { endTimeout } = this.state;

    return (
      <div
        className={cx('n2o-spinner-wrapper', {
          [className]: className,
        })}
      >
        {!endTimeout && loading && (
          <Fragment>
            <div className="n2o-spinner-container ">
              <Comp color={color} {...rest} />
              <div className="loading_text">{text}</div>
            </div>
            {!transparent ? <div className="spinner-background" /> : null}
          </Fragment>
        )}
        {children}
      </div>
    );
  }

  renderLineSpinner() {
    const { endTimeout } = this.state;
    const { type, children, delay, loading, ...rest } = this.props;
    return delay && endTimeout && loading ? (
      <Comp className="spinner" {...rest} />
    ) : React.Children.count(children) ? (
      children
    ) : null;
  }

  render() {
    const { type } = this.props;

    return eq(type, TYPE.COVER)
      ? this.renderCoverSpiner()
      : this.renderLineSpinner();
  }
}

BaseSpinner.propTypes = {
  loading: PropTypes.bool,
  type: PropTypes.oneOf(values(TYPE)),
  delay: PropTypes.number,
  text: PropTypes.string,
  transparent: PropTypes.bool,
  color: PropTypes.string,
};

BaseSpinner.defaultProps = {
  loading: true,
  type: 'inline',
  delay: 400,
  text: '',
  transparent: false,
  color: 'primary',
};

export default BaseSpinner;
