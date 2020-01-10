import React, { Component } from "react";
import PropTypes from "prop-types";
import Button from "reactstrap/lib/Button";
import Row from "reactstrap/lib/Row";
import Col from "reactstrap/lib/Col";

class PrintPage extends Component {
  state = {
    documents: [
      "http://www.constitution.ru/official/pdf/constitution.pdf",
      "https://www.centrattek.ru/media/new/regulation/KOAPRF.pdf",
      "http://www.gubkin.ru/personal_sites/fedotovie/NPA/63.pdf"
    ],
    step: 0
  };
  handleNext = () => {
    this.setState({
      step: ++this.state.step
    });
  };

  handlePrev = () => {
    this.setState({
      step: --this.state.step
    });
  };

  render() {
    return (
      <div>
        <Row>
          <Col>
            <Button
              disabled={!this.state.step}
              color="secondary"
              onClick={this.handlePrev}
            >
              <i className="n2o-icon fa fa-angle-double-left" />
              Назад
            </Button>{" "}
            <Button
              disabled={this.state.step === 2}
              color="secondary"
              onClick={this.handleNext}
            >
              Вперед
              <i className="n2o-icon fa fa-angle-double-right" />
            </Button>
          </Col>
        </Row>
        <br />
        <Row>
          <Col>
            <iframe
              id="printf"
              name="printf"
              src={this.state.documents[this.state.step]}
              width="100%"
              height="650"
            >
              Ваш браузер не поддерживает плавающие фреймы!
            </iframe>
          </Col>
        </Row>
      </div>
    );
  }
}

PrintPage.propTypes = {};
PrintPage.defaultProps = {};

export default PrintPage;
