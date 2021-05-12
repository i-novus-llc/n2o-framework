import React, { Component } from 'react'
import Spinner from 'reactstrap/lib/Spinner'
import classNames from 'classnames'
import eq from 'lodash/eq'
import values from 'lodash/values'
import PropTypes from 'prop-types'

const TYPE = {
    INLINE: 'inline',
    COVER: 'cover',
}

let Comp = Spinner

export class BaseSpinner extends Component {
    constructor(props) {
        super(props)

        this.state = {
            // eslint-disable-next-line react/no-unused-state
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
        const { delay, loading } = this.props

        this.setState({
            // eslint-disable-next-line react/no-unused-state
            loading,
        })

        this.setLoadingWithTimeout(false, delay)
    }

    componentDidUpdate(prevProps) {
        const { loading } = this.props

        if (prevProps.loading !== loading) {
            setTimeout(() => {
                const { loading } = this.props

                this.setState({ showSpinner: loading })
            }, 400)
        }
    }

  setLoadingWithTimeout = (loading, timeout) => {
      // eslint-disable-next-line react/no-unused-state
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
              className={classNames('n2o-spinner-wrapper', {
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

      if (loading) {
          return <Comp className="spinner" {...rest} />
      }
      if (React.Children.count(children)) {
          return children
      }

      return null
  }

  render() {
      const { type } = this.props

      return eq(type, TYPE.COVER)
          ? this.renderCoverSpiner()
          : this.renderLineSpinner()
  }
}

BaseSpinner.propTypes = {
    loading: PropTypes.bool,
    type: PropTypes.oneOf(values(TYPE)),
    delay: PropTypes.number,
    text: PropTypes.string,
    transparent: PropTypes.bool,
    color: PropTypes.string,
    className: PropTypes.string,
    minSpinnerTimeToShow: PropTypes.number,
    children: PropTypes.any,
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
