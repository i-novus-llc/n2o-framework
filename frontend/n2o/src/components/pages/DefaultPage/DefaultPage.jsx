import React, { Component } from 'react';
import { isEmpty, has } from 'lodash';
import PropTypes from 'prop-types';
import { LAYOUTS, REGIONS } from '../../../core/factory/factoryLevels';
import Alert from '../../snippets/Alerts/Alert';
import DocumentTitle from '../../core/DocumentTitle';
import BreadcrumbContainer from '../../core/Breadcrumb/BreadcrumbContainer';
import Actions from '../../actions/Actions';
import Section from '../../layouts/Section';
import Factory from '../../../core/factory/Factory';

class DefaultPage extends Component {
  render() {
    const {
      metadata,
      defaultTemplate: Template = React.Fragment,
      toolbar,
      actions,
      containerKey,
      error,
      pageId,
    } = this.props;

    return (
      <React.Fragment>
        {error && <Alert {...error} visible />}
        {!isEmpty(metadata) && metadata.page && (
          <DocumentTitle {...metadata.page} />
        )}
        {!isEmpty(metadata) && metadata.breadcrumb && (
          <BreadcrumbContainer
            defaultBreadcrumb={this.context.defaultBreadcrumb}
            items={metadata.breadcrumb}
          />
        )}
        {toolbar && (toolbar.topLeft || toolbar.topRight) && (
          <div className="n2o-page-actions">
            <Actions
              toolbar={toolbar.topLeft}
              actions={actions}
              containerKey={containerKey}
              pageId={pageId}
            />
            <Actions
              toolbar={toolbar.topRight}
              actions={actions}
              containerKey={containerKey}
              pageId={pageId}
            />
          </div>
        )}
        <div className="n2o-page">
          {has(metadata, 'layout') && (
            <Factory
              level={LAYOUTS}
              src={metadata.layout.src}
              {...metadata.layout}
            >
              {Object.keys(metadata.layout.regions).map((place, i) => {
                return (
                  <Section place={place} key={'section' + i}>
                    {metadata.layout.regions[place].map(region => (
                      <Factory
                        key={'region' + i}
                        level={REGIONS}
                        {...region}
                        pageId={metadata.id}
                      />
                    ))}
                  </Section>
                );
              })}
            </Factory>
          )}
        </div>
        {toolbar && (toolbar.bottomLeft || toolbar.bottomRight) && (
          <div className="n2o-page-actions">
            <Actions
              toolbar={toolbar.bottomLeft}
              actions={actions}
              containerKey={containerKey}
              pageId={pageId}
            />
            <Actions
              toolbar={toolbar.bottomRight}
              actions={actions}
              containerKey={containerKey}
              pageId={pageId}
            />
          </div>
        )}
      </React.Fragment>
    );
  }
}

DefaultPage.propTypes = {};

export default DefaultPage;
