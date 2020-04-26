<template>
  <div class="about">
    <!-- Hero section -->
    <!--      TODO Docker img names-->
    <section
      class="hero-section set-bg"
      data-setbg="~@/assets/heroImage.jpg"
      :style="{ backgroundImage: 'url(' + heroUrl + ')' }"
    >
      <div class="container">
        <div class="hero-text">
          <h2>Physical activity</h2>
          <h2>enhances <span>positive energy!</span></h2>
          <a href="/membership" class="site-btn">Join the Gym</a>
        </div>
      </div>
    </section>
    <!-- Hero section end -->

    <!-- Feature section -->
    <section class="feature-section">
      <div class="container">
        <div class="row justify-content-md-center">
          <div class="col-lg-8">
            <div class="section-title text-center">
              <h2>
                The real <span>workout</span> starts when you want to stop.
              </h2>
            </div>
          </div>
        </div>
        <div class="row">
          <div class="col-md-4">
            <div class="icon-box-item">
              <div class="ib-icon">
                <img src="../assets/silhouette-3211976.png" alt="" />
              </div>
              <h4>Stay Flexible</h4>
              <p>
                With three state of the art yoga studios.
              </p>
            </div>
          </div>
          <div class="col-md-4">
            <div class="icon-box-item">
              <div class="ib-icon">
                <img src="../assets/athlete-3491526.png" alt="" />
              </div>
              <h4>Stay Strong</h4>
              <p>
                with our two weight rooms.
              </p>
            </div>
          </div>
          <div class="col-md-4">
            <div class="icon-box-item">
              <div class="ib-icon">
                <img src="../assets/silhouette-5010366.png" alt="" />
              </div>
              <h4>Stay Fit</h4>
              <p>
                With the latest equipment and regular classes
              </p>
            </div>
          </div>
        </div>
      </div>
    </section>
    <!-- Feature section end -->

    <!-- Add section -->
    <section
      class="add-section set-bg"
      data-setbg="/img/addsectionImg.80ec09fa.jpg"
      :style="{ backgroundImage: 'url(' + addUrl + ')' }"
    >
      <div class="container">
        <div class="row">
          <div class="col-lg-7 ml-auto">
            <div class="add-text">
              <h2>Become a member and get a 25% <span>Discount</span></h2>
              <a href="/membership" class="site-btn">Become a Member</a>
            </div>
          </div>
        </div>
      </div>
    </section>
    <!-- Add section end -->

    <!-- Milestones section -->
    <section class="milestones-section">
      <div class="container">
        <div class="row">
          <div class="col-lg-3 col-sm-6">
            <div class="milestone">
              <h2>8,904</h2>
              <p>Happy Clients</p>
            </div>
          </div>
          <div class="col-lg-3 col-sm-6">
            <div class="milestone">
              <h2>65</h2>
              <p>Dedicated Employees</p>
            </div>
          </div>
          <div class="col-lg-3 col-sm-6">
            <div class="milestone">
              <h2>32k</h2>
              <p>Instagram Followers</p>
            </div>
          </div>
          <div class="col-lg-3 col-sm-6">
            <div class="milestone">
              <h2>24/7</h2>
              <p>Working Hours</p>
            </div>
          </div>
        </div>
      </div>
    </section>
    <!-- Milestones section end -->

    <!-- Trainers section -->
    <section class="trainers-section">
      <div class="container">
        <div class="section-title text-center">
          <h2>Meet the <span>Trainers</span></h2>
        </div>
        <div class="row">
          <div class="col-lg-3 col-sm-6">
            <div class="trainer-item">
              <div class="trainer-pic">
                <img src="../assets/trainer-1.jpg" alt="" />
              </div>
              <h4>Dennis Newton</h4>
              <p>
                I am an competitive bodybuilder with a wealth knowledge
                concerning building muscle through weight training & nutrition.
              </p>
            </div>
          </div>
          <div class="col-lg-3 col-sm-6">
            <div class="trainer-item">
              <div class="trainer-pic">
                <img src="../assets/trainer-2.jpg" alt="" />
              </div>
              <h4>Greg Dodson</h4>
              <p>
                I offer various martial arts training programmes to help you
                reach your fitness goals and improves your physical health and
                mental well-being
              </p>
            </div>
          </div>
          <div class="col-lg-3 col-sm-6">
            <div class="trainer-item">
              <div class="trainer-pic">
                <img src="../assets/trainer-3.jpg" alt="" />
              </div>
              <h4>Paula Gallagher</h4>
              <p>
                I run yoga classes specialising in back care and injury
                rehabilitation. One to one sessions also available.
              </p>
            </div>
          </div>
          <div class="col-lg-3 col-sm-6">
            <div class="trainer-item">
              <div class="trainer-pic">
                <img src="../assets/trainer-4.jpg" alt="" />
              </div>
              <h4>Codie Weston</h4>
              <p>
                I create successful training programmes for people of all ages
                wanting to lose weight, gain strength or improve their overall
                health!
              </p>
            </div>
          </div>
        </div>
      </div>
    </section>
    <!-- Trainers section end -->

    <!-- BMI section -->
    <section class="bmi-section spad">
      <div
        class="bmi-bg set-bg"
        :data-setbg="foodUrl"
        :style="{ backgroundImage: 'url(' + foodUrl + ')' }"
      ></div>
      <div class="container">
        <div class="row">
          <div class="col-lg-7 ml-auto">
            <div class="section-title mb-0">
              <h2>Calculate your <span>BMI</span></h2>
              <p>
                The Body Mass Index (BMI) is a indicator that uses your height
                and weight to work out if your weight is within a healthy range.
                A Healthy BMI is considered to be between 20 and 25.
              </p>
            </div>
            <div class="bmi-calculator-warp">
              <div class="bmi-calculator">
                <div class="row">
                  <div class="col-sm-6">
                    <b-input-group size="lg" append="KG">
                      <b-form-input
                        step="0.01"
                        id="bmi-weight"
                        name="bmi-weight"
                        v-model="weight"
                        placeholder="Weight"
                        class="form-control"
                      ></b-form-input>
                    </b-input-group>
                  </div>
                  <div class="col-sm-6">
                    <b-input-group size="lg" append="M">
                      <b-form-input
                        step="0.01"
                        id="bmi-hight"
                        name="bmi-hight"
                        v-model="height"
                        placeholder="Height"
                        class="form-control"
                      ></b-form-input>
                    </b-input-group>
                  </div>
                  <div class="col-sm-12">
                    <b-input-group size="lg" prepend="BMI:">
                      <b-form-input
                        type="number"
                        readonly
                        v-model="bmi"
                        id="bmi-result"
                      ></b-form-input>
                    </b-input-group>
                  </div>
                </div>
                <p>
                  Enter your weight and height to automatically calculate your
                  BMI
                </p>
              </div>
            </div>
          </div>
        </div>
      </div>
      <div style="display: none">
        <img src="../assets/heroImg.jpg" ref="heroImg" />
        <img src="../assets/addSectionImg.jpg" ref="addImg" />
        <img src="../assets/healthyFood.jpeg" ref="foodImg" />
      </div>
    </section>
    <!-- BMI section end -->
    <section>
      <div class="container" style="padding-top: 30px">
        <div class="section-title text-center">
          <h2>Find <span>Us</span></h2>
        </div>
      </div>
      <div class="map">
        <iframe
          src="https://www.google.com/maps/embed/v1/place?q=place_id:ChIJ3Sq1l6leeUgRAAH3tEwDbio&key=AIzaSyCeUkdrY6miqihlwvUt_TpWk8XeYM1Z1g0"
          style="border:0"
          allowfullscreen
        ></iframe>
      </div>
    </section>
  </div>
</template>
<script>
// import "~./assests/heroImg.jpg";
export default {
  name: "Home",
  data() {
    return {
      height: null,
      weight: null,

      heroUrl: require('@/assets/heroImg.jpg'),
      addUrl: require('@/assets/addSectionImg.jpg'),
      foodUrl:  require('@/assets/healthyFood.jpeg')
    };
  },
  computed: {
    bmi: function() {
      if (this.weight != null && this.height != null) {
        return (
          Number(this.weight) / (Number(this.height) * Number(this.height))
        );
      } else {
        return 0;
      }
    }
  }
};
</script>
