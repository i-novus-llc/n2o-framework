import React, { memo, useState, useEffect, useMemo } from 'react'
import PropTypes from 'prop-types'
import clsx from 'clsx'

import CONFIG from '../../ci-config.json'
import { Spinner } from '../Spinner/Spinner'
import { Admonition } from '../Admonition/Admonition'

import { visibilityHOC } from './visibilityHOC'
import { CodeWrapper } from './CodeWrapper'
import { NETWORK_PERMISSION_INSTRUCTION, useNetworkPermission } from './useNetworkPermission'
import style from './sandbox.module.scss'

const IS_DOCS = 'isDocs=true'

function SandboxBody({
    projectId,
    height,
    customStyle,
    showHeader,
    showBreadcrumb,
    showFooter,
    className,
    isLightEditor = false,
  }) {
    const [loadError, setLoadError] = useState(null)
    const [projectData, setProjectData] = useState(null)
    const { origin } = window?.location

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
        fetch(`${CONFIG.sandboxUrl}/project/${projectId}?stand=${origin}/sandbox/&${IS_DOCS}`)
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


    const src = useMemo(() => {
        if (!projectData) { return null }

        return isLightEditor
            ? `${CONFIG.sandboxUrl}/editor/${projectData.id}/?light`
            : `${origin}/sandbox/view/${projectData.id}/`
    }, [projectData, isLightEditor, origin])


    const { permissionState, isLoading: permissionLoading } = useNetworkPermission(src)

    // Ошибка проекта
    if (loadError) {
        return <Admonition type="danger" title="Ошибка" text={loadError} />
    }

    // Загрузка
    if (!projectData || permissionLoading) {
        return <Spinner />
    }

    // Запрещен доступ
    if (permissionState === 'denied') {
        return (
            <Admonition
                type="danger"
                title="Доступ запрещён"
                text={NETWORK_PERMISSION_INSTRUCTION}
            />
        )
    }

    function onIframeLoadHandler(event) {
        const message = {
            source: 'docusaurus',
            type: 'SET_N2O_ELEMENT_VISIBILITY',
            payload: {
                header: showHeader,
                breadcrumb: showBreadcrumb || showHeader,
                footer: showFooter,
            },
        }
        event.target.contentWindow.postMessage(message, '*')
    }

    return (
            <>
                <iframe
                        sandbox="allow-scripts allow-same-origin"
                        onLoad={onIframeLoadHandler}
                        style={{ height, ...customStyle }}
                        className={clsx(style.iframe, className)}
                        src={src}
                        allow="local-network *"
                />

                {!isLightEditor && <CodeWrapper projectId={projectId} filesMap={filesMap} />}
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
