import React, { memo, useState, useEffect, useMemo } from 'react'
import PropTypes from 'prop-types'

import CONFIG from '../../ci-config.json'
import { Spinner } from '../Spinner/Spinner'
import { Admonition } from '../Admonition/Admonition'

import { visibilityHOC } from './visibilityHOC'
import { CodeWrapper } from './CodeWrapper'
import style from './sandbox.module.scss'

function SandboxBody({ projectId, height, showHeader, showBreadcrumb, showFooter }) {
    const [loadError, setLoadError] = useState(null)
    const [projectData, setProjectData] = useState(null)

    const filesMap = useMemo(() => {
        const filesMap = {}

        if (projectData) {
            projectData.files.forEach(({ file, source }) => {
                filesMap[file] = source
            })
        }

        return filesMap
    }, [projectData])

    useEffect(() => {
        fetch(`${CONFIG.sandboxUrl}/api/project/${projectId}/`)
            .then((response) => {
                if (response.ok) {
                    return response.json()
                } else {
                    setLoadError('Невозможно загрузить sandbox-проект')
                }
            })
            .then(
                (data) => {
                    setProjectData(data)
                },
                () => {
                    setLoadError('Невозможно загрузить sandbox-проект')
                },
            )
    }, [])

    if (loadError) {
        return <Admonition type="danger" title="Ошибка" text={loadError}/>
    }

    if (!projectData) {
        return <Spinner/>
    }

    function onIframeLoadHandler(event) {
        const message = {
            source: 'docusaurus',
            type: 'SET_N2O_ELEMENT_VISIBILITY',
            payload: {
                header: showHeader,
                breadcrumb: showBreadcrumb,
                footer: showFooter,
            },
        }

        event.target.contentWindow.postMessage(message, '*')
    }

    return (
        <>
            <iframe
                onLoad={onIframeLoadHandler}
                style={{ height }}
                className={style.iframe}
                src={`${CONFIG.sandboxUrl}/view/${projectData.id}/`}
            />

            <CodeWrapper projectId={projectId} filesMap={filesMap} />
        </>
    )
}

SandboxBody.propTypes = {
    projectId: PropTypes.string.isRequired,
    height: PropTypes.number.isRequired,
    showHeader: PropTypes.bool.isRequired,
    showBreadcrumb: PropTypes.bool.isRequired,
    showFooter: PropTypes.bool.isRequired,
}

const Sandbox = visibilityHOC(memo(SandboxBody))

Sandbox.propTypes = {
    projectId: PropTypes.string.isRequired,
    height: PropTypes.number,
    showHeader: PropTypes.bool,
    showBreadcrumb: PropTypes.bool,
    showFooter: PropTypes.bool,
}

Sandbox.defaultProps = {
    showHeader: false,
    showBreadcrumb: false,
    showFooter: false,
}

export { Sandbox }
