import React from 'react'

export interface Props {
    status: number | null
    error?: string
}

const headerStyle = { fontSize: '10rem' }
const spanStyle = { fontSize: '2rem' }

const ErrorPage = ({ status, error }: Props) => {
    return (
        <div className="container d-flex align-items-center justify-content-center">
            <div className="d-flex flex-column align-items-center justify-content-center">
                <h1 style={headerStyle}>{status}</h1>
                <span style={spanStyle}>{error}</span>
            </div>
        </div>
    )
}

export default ErrorPage
