import React, { memo } from 'react'
import classnames from 'classnames'
import PropTypes from 'prop-types'

import CONFIG from '../../ci-config.json'

import style from './sandbox.module.scss'

function HeaderBody({ projectId, activeFileName, setActiveFileName, filesMap }) {
    const fileNames = Object.keys(filesMap)

    let indexFileName = ''

    fileNames.sort((a, b) => a - b)

    if (fileNames.includes('index.page.xml')) {
        indexFileName = 'index.page.xml'
        fileNames.splice(fileNames.indexOf('index.page.xml'), 1)
    } else if (fileNames.length) {
        indexFileName = fileNames[0]
        fileNames.splice(0, 1)
    }

    return (
        <ul className={classnames('tabs', style.headerList)}>
            <a
                className={classnames('tabs__item', style.headerListItem)}
                href={`${CONFIG.sandboxUrl}/new/${projectId}/`}
                target="_blank"
            >
                Sandbox
            </a>

            {
                indexFileName
                    ? (
                        <li
                            className={
                                classnames(
                                    'tabs__item',
                                    style.headerListItem,
                                    {
                                        ['tabs__item--active']: activeFileName === indexFileName,
                                    }
                                )
                            }
                            onClick={() => setActiveFileName(indexFileName)}
                        >
                            {indexFileName}
                        </li>
                    )
                    : null
            }

            {
                fileNames
                    .map(fileName => (
                        <li
                            key={fileName}
                            className={
                                classnames(
                                    'tabs__item',
                                    style.headerListItem,
                                    {
                                        ['tabs__item--active']: activeFileName === fileName,
                                    }
                                )
                            }
                            onClick={() => {
                                setActiveFileName(fileName)
                            }}
                        >
                            {fileName}
                        </li>
                    ))
            }
        </ul>
    )
}

HeaderBody.propTypes = {
    projectId: PropTypes.string.isRequired,
    activeFileName: PropTypes.string.isRequired,
    setActiveFileName: PropTypes.func.isRequired,
    filesMap: PropTypes.objectOf(PropTypes.string).isRequired,
}

const Header = memo(HeaderBody)

Header.propTypes = {
    projectId: PropTypes.string.isRequired,
    activeFileName: PropTypes.string.isRequired,
    setActiveFileName: PropTypes.func.isRequired,
    filesMap: PropTypes.objectOf(PropTypes.string).isRequired,
}

export { Header }
