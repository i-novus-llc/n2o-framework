import React, { Component, Fragment } from 'react'
import Spinner from 'reactstrap/lib/Spinner'
import cx from 'classnames'
import eq from 'lodash/eq'
import values from 'lodash/values'
import PropTypes from 'prop-types'

const TYPE = {
    INLINE: 'inline',
    COVER: 'cover',
}

let Comp = Spinner

class BaseSpinner extends Component {
    constructor(props) {
        super(props)

        this.state = {
            loading: false,
            showSpinner: false,
        }

        this.renderCoverSpiner = this.renderCoverSpiner.bind(this)
        this.renderLineSpinner = this.renderLineSpinner.bind(this)
    }

    static setSpinner(component) {
        Comp = component
    }

    componentDidMount() {
        this.setState({
            loading: this.props.loading,
        })
        const { delay } = this.props
        this.setLoadingWithTimeout(false, delay)
    }

    componentDidUpdate(prevProps, prevState) {
        if (prevProps.loading !== this.props.loading) {
            setTimeout(() => this.setState({ showSpinner: this.props.loading }), 400)
        }
    }

  setLoadingWithTimeout = (loading, timeout) => {
      setTimeout(() => this.setState({ loading }), timeout)
  };

  renderCoverSpiner() {
      const {
          children,
          className,
          text,
          transparent,
          color,
          loading,
          ...rest
      } = this.props
      const { showSpinner } = this.state
      return (
          <div
              className={cx('n2o-spinner-wrapper', {
                  [className]: className,
              })}
          >
              {showSpinner && (
                  <>
                      <div className="n2o-spinner-container ">
                          <Comp className="spinner-border" color={color} {...rest} />
                          <div className="loading_text">{text}</div>
                      </div>
                      {!transparent ? <div className="spinner-background" /> : null}
                  </>
              )}
              {children}
          </div>
      )
  }

  renderLineSpinner() {
      const { type, children, delay, loading, ...rest } = this.props

      return loading ? (
          <Comp className="spinner" {...rest} />
      ) : React.Children.count(children) ? (
          children
      ) : null
  }

  render() {
      const { type } = this.props

      return eq(type, TYPE.COVER)
          ? this.renderCoverSpiner()
          : this.renderLineSpinner()
  }

  static setState(state1) {}
}

BaseSpinner.propTypes = {
    loading: PropTypes.bool,
    type: PropTypes.oneOf(values(TYPE)),
    delay: PropTypes.number,
    text: PropTypes.string,
    transparent: PropTypes.bool,
    color: PropTypes.string,
    minSpinnerTimeToShow: PropTypes.number,
}

BaseSpinner.defaultProps = {
    loading: true,
    type: 'inline',
    delay: 400,
    text: '',
    transparent: false,
    color: 'primary',
    minSpinnerTimeToShow: 250,
}

export default BaseSpinner
