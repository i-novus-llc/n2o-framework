import React, { PureComponent } from 'react'
import { withTranslation } from 'react-i18next'
import PropTypes from 'prop-types'

class Page404 extends PureComponent {
    render() {
        const { t } = this.props

        return <h1>{t('pageNotFound')}</h1>
    }
}

Page404.propTypes = {
    t: PropTypes.func,
}
Page404.defaultProps = {}

export default withTranslation()(Page404)
