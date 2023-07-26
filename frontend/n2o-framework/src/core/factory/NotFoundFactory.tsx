import React from 'react'
import { Alert } from 'reactstrap'

interface INotFoundFactory {
    src: string
    level: string
}

export function NotFoundFactory(props: INotFoundFactory) {
    const { src, level } = props

    /* eslint-disable react/jsx-one-expression-per-line */
    return (
        <Alert color="danger">

      Фабрике не удалось найти компонент:
            {src}
            {' '}

в
            {level}
        </Alert>
    )
}

export default NotFoundFactory
