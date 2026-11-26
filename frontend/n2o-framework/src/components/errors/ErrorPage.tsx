import React from 'react'
import { Text } from '@i-novus/n2o-components/lib/Typography/Text'

interface ContainerError {
    title: number | string
    status: number
    text?: string
}

export interface Props {
    status: number | string | null
    error?: string | ContainerError
}

const ErrorPage = ({ status, error }: Props) => {
    const errorText = typeof error === 'object' ? error?.text : error

    return (
        <div className="container d-flex align-items-center justify-content-center">
            <div className="d-flex flex-column align-items-center justify-content-center">
                <h1 style={{ fontSize: '2rem', fontWeight: 'bold' }}>{status}</h1>
                {errorText && <span style={{ fontSize: '1.3rem' }}><Text>{errorText}</Text></span>}
            </div>
        </div>
    )
}

export default ErrorPage
