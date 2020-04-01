package uk.ac.leeds.comp2913.api.DataAccessLayer.Repository.Impl;

import org.hibernate.transform.Transformers;
import org.hibernate.type.LongType;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.data.rest.core.mapping.ResourceType;
import uk.ac.leeds.comp2913.api.DataAccessLayer.Repository.CustomActivityRepository;
import uk.ac.leeds.comp2913.api.Domain.Model.Activity;
import uk.ac.leeds.comp2913.api.Domain.Model.Resource;
import uk.ac.leeds.comp2913.api.ViewModel.ActivityDTO;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.swing.text.html.Option;
import java.util.Optional;

public class CustomActivityRepositoryImpl implements CustomActivityRepository {
}
