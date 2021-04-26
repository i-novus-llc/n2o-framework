import React from 'react'
import PropTypes from 'prop-types'

const headerStyle = { fontSize: '10rem' }
const spanStyle = { fontSize: '2rem' }

function ErrorPage({ status, error }) {
    return (
        <div className="container d-flex align-items-center justify-content-center">
            <div className="d-flex flex-column align-items-center justify-content-center">
                <h1 style={headerStyle}>{status}</h1>
                <span style={spanStyle}>{error}</span>
            </div>
        </div>
    )
}

ErrorPage.propTypes = {
    status: PropTypes.number,
    error: PropTypes.string,
}

export default ErrorPage
