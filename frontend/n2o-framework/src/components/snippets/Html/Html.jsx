import React from 'react'
import PropTypes from 'prop-types'
import classNames from 'classnames'

export const Html = ({ id, html, className }) => (
    <div id={id} className={classNames('n2o-html-snippet n2o-snippet', className)}>
        {/* eslint-disable-next-line react/no-danger */}
        <div dangerouslySetInnerHTML={{ __html: html }} />
    </div>
)

Html.propTypes = {
    id: PropTypes.string,
    html: PropTypes.string,
    className: PropTypes.string,
}
