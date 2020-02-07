import React from "react";
import { isEqual, map } from "lodash";
import Row from "reactstrap/lib/Row";
import Col from "reactstrap/lib/Col";
import Button from "reactstrap/lib/Button";
import Jumbotron from "reactstrap/lib/Jumbotron";
import ReduxField from "n2o-framework/lib/components/widgets/Form/ReduxField";
import PropTypes from "prop-types";

class AddMemberFieldset extends React.Component {
  constructor(props) {
    super(props);

    this.state = {
      rows: props.rows
    };

    this.template = props.rows;

    this.addMember = this.addMember.bind(this);
    this.createNewRow = this.createNewRow.bind(this);
  }

  componentDidUpdate(prevProps) {
    if (!isEqual(prevProps.rows, this.props.rows)) {
      this.setState({ rows: this.props.rows });
    }
  }

  createNewRow() {
    return map(this.template, ({ cols }) => ({
      cols: map(cols, ({ fields }) => ({
        fields: map(fields, item => ({
          ...item,
          id: item.id + this.state.rows.length
        }))
      }))
    }));
  }

  addMember() {
    let rows = [...this.state.rows];
    const template = this.createNewRow();
    rows = rows.concat(template);
    this.setState({
      rows
    });
  }

  renderFields(rows) {
    return (
      <Jumbotron className="mb-0 p-4">
        {map(rows, ({ cols }) => (
          <Row>
            {map(cols, ({ fields }) => (
              <Col md={6}>
                {map(fields, item => (
                  <ReduxField {...item} />
                ))}
              </Col>
            ))}
          </Row>
        ))}
      </Jumbotron>
    );
  }

  render() {
    return (
      <div>
        <div>
          <div
            style={{
              display: "flex"
            }}
          >
            <div style={{ flexGrow: 1 }}>
              {this.renderFields(this.state.rows)}
            </div>
            <div
              className="ml-4"
              style={{
                alignSelf: "flex-end",
              }}
            >
              <Button
                color="success"
                onClick={this.addMember}
                size={"sm"}
                style={{ width: 38, height: 38 }}
              >
                <i className="fa fa-plus-circle" />
              </Button>
            </div>
          </div>
        </div>
      </div>
    );
  }
}

AddMemberFieldset.contextTypes = {
  resolveProps: PropTypes.func
};

export default AddMemberFieldset;
