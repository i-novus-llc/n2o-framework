import React from 'react'
import PropTypes from 'prop-types'
import classNames from 'classnames'

// eslint-disable-next-line react/no-danger
export const Html = ({ id, html, className }) => <div id={id} className={classNames('n2o-html-snippet n2o-snippet', className)} dangerouslySetInnerHTML={{ __html: html }} />

Html.propTypes = {
    id: PropTypes.string,
    html: PropTypes.string,
    className: PropTypes.string,
}
