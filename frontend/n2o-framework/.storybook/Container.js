import React, { Component } from 'react';
import { pick, keys } from 'lodash';
import { Card, CardBody } from 'reactstrap';
import withProviders from './decorators/ProvidersDecorator';
export default class Container extends Component {
  getParsedPath() {
    const arrKind = this.props.path.kind.split('/');
    arrKind.push(this.props.path.story);
    return arrKind;
  }

  render() {
    const { story, path } = this.props;
    return (
      <div style={{ padding: '2rem' }}>
        <nav aria-label="breadcrumb">
          <ol className="breadcrumb">
            {this.getParsedPath().map(val => (
              <li className="breadcrumb-item active">{val}</li>
            ))}
          </ol>
        </nav>
        <Card
          style={{
            display: 'flex',
            flexDirection: 'column',
            alignItems: 'center',
          }}
        >
          <CardBody style={{ minWidth: '90%', flexBasis: 'auto', flexGrow: 1 }}>
            {withProviders(story)}
          </CardBody>
        </Card>
      </div>
    );
  }
}
