import React, { memo, useState, useEffect } from 'react'
import classnames from 'classnames'
import PropTypes from 'prop-types'

import CONFIG from '../../ci-config.json'
import { Spinner } from '../Spinner/Spinner'
import { Admonition } from '../Admonition/Admonition'

import { visibilityHOC } from './visibilityHOC'
import style from './sandbox.module.scss'

function SandboxBody({ projectId }) {
    const [loadError, setLoadError] = useState(null)
    const [projectData, setProjectData] = useState(null)
    const [activeFileName, setActiveFileName] = useState('')

    useEffect(() => {
        fetch(`${CONFIG.sandboxHost}/api/project/${projectId}/`)
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

    const activeFile = projectData.files.filter(({ file }) => file === activeFileName)[0]

    return (
        <>
            <div>
                <ul className={classnames('tabs', style.headerList)}>
                    <li
                        className={classnames('tabs__item', style.headerListItem, { ['tabs__item--active']: activeFileName === '' })}
                        onClick={() => {
                            setActiveFileName('')
                        }}
                    >
                        N2O
                    </li>
                    {
                        projectData.files
                            .map(({ file }) => file)
                            .map(fileName => (
                                <li
                                    key={fileName}
                                    className={classnames('tabs__item', style.headerListItem, { ['tabs__item--active']: activeFileName === fileName })}
                                    onClick={() => {
                                        setActiveFileName(fileName)
                                    }}
                                >
                                    {fileName}
                                </li>
                            ))
                    }

                </ul>
            </div>
            <iframe className={classnames(style.iframe, { [style.active]: activeFileName === '' })}
                    src={`${CONFIG.sandboxHost}${projectData.viewUrl}`}/>
            {
                activeFile
                    ? (
                        <div className={style.fileView}>
                            {activeFile.source}
                        </div>
                    )
                    : null
            }
        </>
    )
}

const Sandbox = visibilityHOC(memo(SandboxBody))

Sandbox.propTypes = {
    projectId: PropTypes.string.isRequired,
    height: PropTypes.number,
}

export { Sandbox }
