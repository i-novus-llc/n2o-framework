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
    const { children, className, text, loading, type, ...rest } = this.props;
    const { endTimeout } = this.state;
    return (
      <div
        className={cx('n2o-spinner-wrapper', {
          [className]: className,
        })}
      >
        {endTimeout && loading && (
          <Fragment>
            <div className="n2o-spinner-container ">
              <Comp color="primary" {...rest} />
              <div className="loading_text">{text}</div>
            </div>
            <div className="spinner-background" />
          </Fragment>
        )}
        {children}
      </div>
    );
  }

  renderLineSpinner() {
    const { type, ...rest } = this.props;
    return <Comp {...rest} />;
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
};

BaseSpinner.defaultProps = {
  loading: true,
  type: 'inline',
  delay: 400,
  text: '',
};

export default BaseSpinner;
