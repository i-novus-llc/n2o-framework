import React, { Component } from 'react'
import { withTranslation } from 'react-i18next'

class Page404 extends Component {
    render() {
        return <h1>{this.props.t('pageNotFound')}</h1>
    }
}

Page404.propTypes = {}
Page404.defaultProps = {}

export default withTranslation()(Page404)
