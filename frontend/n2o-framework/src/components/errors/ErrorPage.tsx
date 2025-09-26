import React from 'react'
import { Text } from '@i-novus/n2o-components/lib/Typography/Text'

export interface Props {
    status: number | string | null
    error?: string
}

const ErrorPage = ({ status, error }: Props) => {
    return (
        <div className="container d-flex align-items-center justify-content-center">
            <div className="d-flex flex-column align-items-center justify-content-center">
                <h1 style={{ fontSize: '2rem', fontWeight: 'bold' }}>{status}</h1>
                <span style={{ fontSize: '1.3rem' }}><Text>{error}</Text></span>
            </div>
        </div>
    )
}

export default ErrorPage
